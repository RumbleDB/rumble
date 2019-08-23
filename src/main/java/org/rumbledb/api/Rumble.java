package org.rumbledb.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.spark.api.java.JavaRDD;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.ParsingException;
import sparksoniq.jsoniq.compiler.JsoniqExpressionTreeVisitor;
import sparksoniq.jsoniq.compiler.parser.JsoniqLexer;
import sparksoniq.jsoniq.compiler.parser.JsoniqParser;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.visitor.RuntimeIteratorVisitor;
import sparksoniq.semantics.visitor.StaticContextVisitor;
import sparksoniq.spark.SparkSessionManager;

/**
 * The entry point for Java applications that want to executed JSONiq queries with Rumble.
 * 
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public class Rumble {
	
	private RumbleConf _conf;
	
	/**
	 * Creates a new Rumble instance.
         *
         * @param conf a RumbleConf object containing the configuration.
	 */
	public Rumble(RumbleConf conf)
	{
		_conf = conf;
        SparkSessionManager.COLLECT_ITEM_LIMIT = conf.getResultsSizeCap();
	}
	
	/**
	 * Runs a query and returns an iterator over the resulting sequence of Items.
	 * @param query the JSONiq query.
	 * @return the resulting sequence as an ItemIterator.
	 */
	public ItemIterator runQuery(String query)
	{
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
            ParsingException e = new ParsingException(lexer.getText(), new ExpressionMetadata(lexer.getLine(),
                    lexer.getCharPositionInLine()));
            e.initCause(ex);
            throw e;
        }
        Expression expression = visitor.getQueryExpression();
        new StaticContextVisitor().visit(expression, expression.getStaticContext());

        RuntimeIterator iterator = new RuntimeIteratorVisitor().visit(expression, null);
        return new ItemIterator(iterator);
	}
}
