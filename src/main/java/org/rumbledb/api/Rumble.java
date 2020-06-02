package org.rumbledb.api;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.rumbledb.compiler.TranslationVisitor;
import org.rumbledb.compiler.VisitorHelpers;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.parser.JsoniqLexer;
import org.rumbledb.parser.JsoniqParser;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

/**
 * The entry point for Java applications that want to execute JSONiq queries with Rumble.
 *
 * The query must be provided as a string and a sequence of items is returned.
 *
 * It is possible for the queries to use the text-file() and json-file() functions if Spark and either the local file
 * system or HDFS are properly configured.
 *
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public class Rumble {

    private RumbleConf conf;

    /**
     * Creates a new Rumble instance. This does NOT initialize Spark. You need to do so before instantiating Rumble.
     *
     * @param conf a RumbleConf object containing the configuration.
     */
    public Rumble(RumbleConf conf) {
        this.conf = conf;
        SparkSessionManager.COLLECT_ITEM_LIMIT = this.conf.getResultsSizeCap();
    }

    /**
     * Runs a query and returns an iterator over the resulting sequence of Items.
     *
     * @param query the JSONiq query.
     * @return the resulting sequence as an ItemIterator.
     */
    public SequenceOfItems runQuery(String query) {
        CharStream charStream = CharStreams.fromString(query);
        JsoniqLexer lexer = new JsoniqLexer(charStream);
        JsoniqParser parser = new JsoniqParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        TranslationVisitor visitor = new TranslationVisitor();
        MainModule mainModule = null;
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext module = parser.module();
            JsoniqParser.MainModuleContext main = module.main;
            mainModule = (MainModule) visitor.visit(main);
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExceptionMetadata(
                            lexer.getLine(),
                            lexer.getCharPositionInLine()
                    )
            );
            e.initCause(ex);
            throw e;
        }
        VisitorHelpers.resolveDependencies(mainModule, RumbleRuntimeConfiguration.getDefaultConfiguration());
        VisitorHelpers.populateStaticContext(mainModule, RumbleRuntimeConfiguration.getDefaultConfiguration());

        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(mainModule);
        return new SequenceOfItems(iterator);
    }
}
