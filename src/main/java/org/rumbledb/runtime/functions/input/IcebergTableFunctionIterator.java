package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IcebergTableFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public IcebergTableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator collectionNameIterator = this.children.get(0);
        String collectionName = collectionNameIterator.materializeFirstItemOrNull(context).getStringValue();

        String metadataName = qualifyForMetadata(collectionName);
        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().table(collectionName);
        return DeltaTableFunctionIterator.postProcess(dataFrame, metadataName);
    }

    /**
     * Qualifies the collection name for metadata retrieval using Iceberg's Spark
     * resolution order for catalog / namespace / table identifiers.
     * See https://iceberg.apache.org/docs/1.10.0/spark-queries/#catalogs-with-dataframereader
     *
     * Note: Spark's public Catalog API only exposes a single current namespace
     * via currentDatabase(), so we treat it as a single namespace component here.
     */
    private static String qualifyForMetadata(String collectionName) {
        List<String> parts = splitIdentifier(collectionName);
        if (parts.isEmpty()) {
            return collectionName;
        }

        String currentCatalog = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .catalog()
            .currentCatalog();
        String currentNamespace = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .catalog()
            .currentDatabase();

        if (parts.size() == 1) {
            assertIcebergCatalog(currentCatalog, collectionName);
            return joinIdentifier(currentCatalog, new String[] { currentNamespace }, parts.get(0));
        }

        if (isKnownCatalog(parts.get(0))) {
            String catalog = parts.get(0);
            if (parts.size() == 2) {
                assertIcebergCatalog(catalog, collectionName);
                return joinIdentifier(catalog, new String[] { currentNamespace }, parts.get(1));
            }
            String[] namespace = parts.subList(1, parts.size() - 1).toArray(new String[0]);
            assertIcebergCatalog(catalog, collectionName);
            return joinIdentifier(catalog, namespace, parts.get(parts.size() - 1));
        }

        String[] namespace = parts.subList(0, parts.size() - 1).toArray(new String[0]);
        assertIcebergCatalog(currentCatalog, collectionName);
        return joinIdentifier(currentCatalog, namespace, parts.get(parts.size() - 1));
    }

    private static boolean isKnownCatalog(String name) {
        Set<String> catalogs = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .catalog()
            .listCatalogs()
            .collectAsList()
            .stream()
            .map(c -> c.name())
            .collect(Collectors.toSet());
        return catalogs.contains(name);
    }

    private static void assertIcebergCatalog(String catalogName, String collectionName) {
        if (isIcebergCatalog(catalogName)) {
            return;
        }
        Set<String> icebergCatalogs = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .catalog()
            .listCatalogs()
            .collectAsList()
            .stream()
            .map(c -> c.name())
            .filter(IcebergTableFunctionIterator::isIcebergCatalog)
            .collect(Collectors.toSet());
        throw new RuntimeException(
                "Iceberg catalog '"
                    + catalogName
                    + "' is not configured for iceberg-table(\""
                    + collectionName
                    + "\"). Use a configured Iceberg catalog ("
                    + String.join(", ", icebergCatalogs)
                    + ") and fully qualify as <catalog>.<namespace>.<table>."
        );
    }

    private static boolean isIcebergCatalog(String catalogName) {
        String key = "spark.sql.catalog." + catalogName;
        String impl = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .conf()
            .get(key, "");
        return "org.apache.iceberg.spark.SparkCatalog".equals(impl)
            || "org.apache.iceberg.spark.SparkSessionCatalog".equals(impl);
    }



    private static List<String> splitIdentifier(String identifier) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inBackticks = false;
        for (int i = 0; i < identifier.length(); i++) {
            char c = identifier.charAt(i);
            if (c == '`') {
                if (inBackticks && i + 1 < identifier.length() && identifier.charAt(i + 1) == '`') {
                    current.append('`');
                    i++;
                } else {
                    inBackticks = !inBackticks;
                }
                continue;
            }
            if (c == '.' && !inBackticks) {
                parts.add(current.toString());
                current.setLength(0);
                continue;
            }
            current.append(c);
        }
        parts.add(current.toString());
        return parts;
    }

    private static String joinIdentifier(String catalog, String[] namespace, String table) {
        StringBuilder sb = new StringBuilder();
        sb.append(catalog);
        for (String ns : namespace) {
            sb.append('.').append(ns);
        }
        sb.append('.').append(table);
        return sb.toString();
    }
}
