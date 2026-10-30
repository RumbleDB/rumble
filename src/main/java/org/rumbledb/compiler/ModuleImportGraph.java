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
import java.util.ArrayList;
import java.util.Comparator;
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
        List<LibraryModule> modules = new ArrayList<>(new LinkedHashSet<>(session.modules.values()));
        modules.forEach(graph::addVertex);
        Map<LibraryModule, LibraryModule> canonical = new IdentityHashMap<>();
        Map<LibraryModule, URI> sourceLocations = new IdentityHashMap<>();
        session.modules.forEach((location, module) -> sourceLocations.put(module, location));
        for (LibraryModule module : modules) {
            canonical.put(module, module);
            for (LibraryModule imported : module.getProlog().getImportedModules()) {
                // Diagnostic locations may be overridden by a shared static base URI.
                // Back edges must resolve through the loader's module identities instead.
                LibraryModule target = session.completedModules.getOrDefault(imported, imported);
                if (!graph.containsVertex(target)) {
                    throw new IllegalStateException(
                            "Imported module has no completed translation: " + imported.getNamespace());
                }
                canonical.put(imported, target);
                graph.addEdge(module, target);
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
            LibraryModule group = groups.get(canonical.getOrDefault(root, root));
            // Preserve each importing prefix's namespace when a group spans multiple namespaces.
            LibraryModule result = new LibraryModule(group.getProlog(), root.getNamespace(), group.getMetadata());
            result.setStaticContext(group.getStaticContext());
            results.add(result);
        }
        return results;
    }
}
