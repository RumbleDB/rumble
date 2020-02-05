package org.rumbledb.api;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.rumbledb.exceptions.ParsingException;

import sparksoniq.jsoniq.compiler.JsoniqExpressionTreeVisitor;
import sparksoniq.jsoniq.compiler.parser.JsoniqLexer;
import sparksoniq.jsoniq.compiler.parser.JsoniqParser;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.visitor.VisitorHelpers;
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

    private RumbleConf _conf;

    /**
     * Creates a new Rumble instance. This does NOT initialize Spark. You need to do so before instantiating Rumble.
     *
     * @param conf a RumbleConf object containing the configuration.
     */
    public Rumble(RumbleConf conf) {
        _conf = conf;
        SparkSessionManager.COLLECT_ITEM_LIMIT = conf.getResultsSizeCap();
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
        JsoniqExpressionTreeVisitor visitor = new JsoniqExpressionTreeVisitor();
        try {
            // TODO Handle module extras
            JsoniqParser.ModuleContext module = parser.module();
            JsoniqParser.MainModuleContext main = module.main;
            visitor.visit(main);
        } catch (ParseCancellationException ex) {
            ParsingException e = new ParsingException(
                    lexer.getText(),
                    new ExpressionMetadata(
                            lexer.getLine(),
                            lexer.getCharPositionInLine()
                    )
            );
            e.initCause(ex);
            throw e;
        }
        Expression expression = visitor.getMainModule();
        VisitorHelpers.populateStaticContext(expression);

        RuntimeIterator iterator = VisitorHelpers.generateRuntimeIterator(expression);
        return new SequenceOfItems(iterator);
    }
}
