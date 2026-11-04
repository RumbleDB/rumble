/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;

/** Turns an import graph into an acyclic tree of declaration groups before compiler visitors run. */
final class ModuleImportGraph {
    private ModuleImportGraph() {}

    static LibraryModule normalize(
            LibraryModule root, ModuleImportLoader.ImportSession session, RumbleConfiguration configuration) {
        return normalize(List.of(root), session, configuration).get(0);
    }

    static List<LibraryModule> normalize(
            List<LibraryModule> roots, ModuleImportLoader.ImportSession session, RumbleConfiguration configuration) {
        var graph = new DefaultDirectedGraph<LibraryModule, DefaultEdge>(DefaultEdge.class);
        List<LibraryModule> modules = findReachableModules(roots, session);
        modules.forEach(graph::addVertex);
        Map<LibraryModule, URI> sourceLocations = new IdentityHashMap<>();
        session.modules.forEach((location, module) -> sourceLocations.put(module, location));
        for (LibraryModule module : modules) {
            for (LibraryModule imported : module.getProlog().getImportedModules()) {
                graph.addEdge(module, resolveImport(imported, session));
            }
        }
        // Fragments with one target namespace form one module, including same-namespace back edges.
        Map<String, LibraryModule> namespaces = new LinkedHashMap<>();
        for (LibraryModule module : modules) {
            LibraryModule first = namespaces.putIfAbsent(module.getNamespace(), module);
            if (first != null) {
                graph.addEdge(first, module);
                graph.addEdge(module, first);
            }
        }
        Map<LibraryModule, LibraryModule> groups = new IdentityHashMap<>();
        Set<LibraryModule> representatives = new LinkedHashSet<>();
        for (Set<LibraryModule> component : new KosarajuStrongConnectivityInspector<>(graph).stronglyConnectedSets()) {
            List<LibraryModule> members = component.stream()
                    .sorted(Comparator.comparing(
                            module -> sourceLocations.get(module).toString()))
                    .toList();
            LibraryModule first = members.get(0);
            LibraryModule representative = first;
            if (members.size() > 1 || graph.containsEdge(first, first)) {
                Prolog prolog = new Prolog(
                        List.of(), List.of(), List.of(), first.getProlog().getMetadata());
                for (LibraryModule member : members) {
                    prolog.getDeclarations().addAll(member.getProlog().getDeclarations());
                    prolog.getSchemaImports().addAll(member.getProlog().getSchemaImports());
                    for (var declaration : member.getProlog().getDeclarations()) {
                        prolog.getSourceModuleContexts().put(declaration, member.getStaticContext());
                    }
                }
                representative = new LibraryModule(prolog, first.getNamespace(), first.getMetadata());
                representative.setStaticContext(first.getStaticContext());
            }
            // Pruning must distinguish modules even when their configured base URI is identical.
            representative
                    .getProlog()
                    .setModuleGroupKey(members.stream()
                            .map(module -> sourceLocations.get(module).toString())
                            .collect(Collectors.joining("|")));
            representatives.add(representative);
            for (LibraryModule member : members) {
                groups.put(member, representative);
            }
        }
        // Snapshot the edges before replacing any prologs, since singleton groups retain their AST.
        Map<LibraryModule, Set<LibraryModule>> imports = new IdentityHashMap<>();
        representatives.forEach(group -> imports.put(group, new LinkedHashSet<>()));
        for (DefaultEdge edge : graph.edgeSet()) {
            LibraryModule from = groups.get(graph.getEdgeSource(edge));
            LibraryModule to = groups.get(graph.getEdgeTarget(edge));
            if (from != to) {
                imports.get(from).add(to);
            }
        }
        for (LibraryModule group : representatives) {
            group.getProlog().getImportedModules().clear();
            group.getProlog().getImportedModules().addAll(imports.get(group));
            new VariableDependenciesVisitor(configuration).visit(group.getProlog(), null);
        }
        List<LibraryModule> results = new ArrayList<>();
        for (LibraryModule root : roots) {
            LibraryModule group = groups.get(completedModule(root, session));
            // Preserve each importing prefix's namespace when a group spans multiple namespaces.
            LibraryModule result = new LibraryModule(group.getProlog(), root.getNamespace(), group.getMetadata());
            result.setStaticContext(group.getStaticContext());
            results.add(result);
        }
        return results;
    }

    /** Failed location hints can leave translated modules behind, which are not part of the query. */
    private static List<LibraryModule> findReachableModules(
            List<LibraryModule> roots, ModuleImportLoader.ImportSession session) {
        Map<String, List<LibraryModule>> fragments =
                session.modules.values().stream().collect(Collectors.groupingBy(LibraryModule::getNamespace));
        Set<LibraryModule> reachable = new LinkedHashSet<>();
        Deque<LibraryModule> pending = new ArrayDeque<>();
        roots.forEach(root -> pending.add(completedModule(root, session)));
        while (!pending.isEmpty()) {
            LibraryModule module = pending.poll();
            if (reachable.add(module)) {
                pending.addAll(fragments.get(module.getNamespace()));
                module.getProlog()
                        .getImportedModules()
                        .forEach(imported -> pending.add(resolveImport(imported, session)));
            }
        }
        return new ArrayList<>(reachable);
    }

    /** Diagnostic locations may be overridden by a shared static base URI, so follow the loader's identities. */
    private static LibraryModule completedModule(LibraryModule module, ModuleImportLoader.ImportSession session) {
        LibraryModule completed = module;
        while (session.completedModules.containsKey(completed)) {
            completed = session.completedModules.get(completed);
        }
        return completed;
    }

    private static LibraryModule resolveImport(LibraryModule imported, ModuleImportLoader.ImportSession session) {
        LibraryModule target = completedModule(imported, session);
        if (session.modules.containsValue(target)) {
            return target;
        }
        // The location this import named failed to load, but another location may supply its namespace.
        return session.modules.values().stream()
                .filter(module -> module.getNamespace().equals(target.getNamespace()))
                .findFirst()
                .orElseThrow(() -> new ModuleNotFoundException(
                        "Module not found: " + target.getNamespace(), target.getMetadata()));
    }
}
