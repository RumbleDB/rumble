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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import org.rumbledb.exceptions.ModuleNotFoundException;
import org.rumbledb.expressions.module.LibraryModule;
import org.rumbledb.expressions.module.Prolog;

/**
 * Removes cycles from the import graph before compiler visitors run. Imports name a target namespace, so the graph
 * connects namespaces. Each set of namespaces importing each other in a cycle becomes one group of declarations.
 */
final class ModuleImportGraph {
    private ModuleImportGraph() {}

    static List<LibraryModule> normalize(List<LibraryModule> roots, Collection<LibraryModule> loadedModules) {
        Map<String, List<LibraryModule>> modulesByNamespace =
                loadedModules.stream().collect(Collectors.groupingBy(LibraryModule::getNamespace));
        Graph<String, DefaultEdge> graph = buildNamespaceGraph(roots, modulesByNamespace);

        Map<String, LibraryModule> groups = new HashMap<>();
        for (Set<String> namespaces : new KosarajuStrongConnectivityInspector<>(graph).stronglyConnectedSets()) {
            LibraryModule group = merge(loadedModules.stream()
                    .filter(module -> namespaces.contains(module.getNamespace()))
                    .toList());
            group.getProlog().getImportedModules().clear();
            namespaces.forEach(namespace -> groups.put(namespace, group));
        }
        for (DefaultEdge edge : graph.edgeSet()) {
            LibraryModule importing = groups.get(graph.getEdgeSource(edge));
            LibraryModule imported = groups.get(graph.getEdgeTarget(edge));
            List<LibraryModule> imports = importing.getProlog().getImportedModules();
            if (importing != imported && !imports.contains(imported)) {
                imports.add(imported);
            }
        }
        return roots.stream()
                .map(root -> groups.get(root.getNamespace()))
                .distinct()
                .toList();
    }

    /** Only namespaces reachable from the roots: failed location hints can leave unrelated modules behind. */
    private static Graph<String, DefaultEdge> buildNamespaceGraph(
            List<LibraryModule> roots, Map<String, List<LibraryModule>> modulesByNamespace) {
        Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        Deque<String> pending = new ArrayDeque<>();
        for (LibraryModule root : roots) {
            if (graph.addVertex(root.getNamespace())) {
                pending.add(root.getNamespace());
            }
        }
        while (!pending.isEmpty()) {
            String namespace = pending.poll();
            for (LibraryModule module : modulesByNamespace.get(namespace)) {
                for (LibraryModule imported : module.getProlog().getImportedModules()) {
                    String target = imported.getNamespace();
                    if (!modulesByNamespace.containsKey(target)) {
                        throw new ModuleNotFoundException("Module not found: " + target, imported.getMetadata());
                    }
                    if (graph.addVertex(target)) {
                        pending.add(target);
                    }
                    graph.addEdge(namespace, target);
                }
            }
        }
        return graph;
    }

    /** Each declaration keeps its own module's static context, so private names and settings stay per module. */
    private static LibraryModule merge(List<LibraryModule> members) {
        LibraryModule first = members.get(0);
        if (members.size() == 1) {
            return first;
        }
        Prolog prolog = new Prolog(first.getProlog().getMetadata());
        for (LibraryModule member : members) {
            prolog.getSchemaImports().addAll(member.getProlog().getSchemaImports());
            for (var declaration : member.getProlog().getDeclarations()) {
                prolog.addDeclaration(declaration);
                prolog.getSourceModuleContexts().put(declaration, member.getStaticContext());
            }
        }
        LibraryModule group = new LibraryModule(prolog, first.getNamespace(), first.getMetadata());
        group.setStaticContext(first.getStaticContext());
        return group;
    }
}
