package sparksoniq.jsoniq.runtime.iterator.functions.strings;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class TokenizeFunctionIterator extends LocalFunctionCallIterator {
    public TokenizeFunctionIterator(List<RuntimeIterator> arguments, IteratorMetadata iteratorMetadata) {
        super(arguments, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (_nextResult != null) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "tokenize function", getMetadata());
    }


    @Override
    public void open(DynamicContext context) {
        super.open(context);
        _results = null;
        _currentPosition = -1;
        setNextResult();
    }

    public void setNextResult() {
      if(_results == null)
      {
          // Getting first parameter
          RuntimeIterator stringIterator = _children.get(0);
          stringIterator.open(_currentDynamicContext);
          if(!stringIterator.hasNext())
          {
              _hasNext = false;
              stringIterator.close();
              return;
          }
          String input = null;
          String separator = null;
          Item stringItem = stringIterator.next();
          if(stringIterator.hasNext())
              throw new UnexpectedTypeException("First parameter of tokenize must be a string or the empty sequence.", getMetadata());
          stringIterator.close();
          if(!stringItem.isString())
              throw new UnexpectedTypeException("First parameter of tokenize must be a string or the empty sequence.", getMetadata());
          try {
              input = stringItem.getStringValue();
              stringIterator.close();
          } catch (Exception e) {
              throw new UnexpectedTypeException("First parameter of tokenize must be a string or the empty sequence.", getMetadata());
          }

          // Getting second parameter
          if(_children.size() == 1)
          {
              separator = "\\s+";
          } else {
              RuntimeIterator separatorIterator = _children.get(1);
              separatorIterator.open(_currentDynamicContext);
              if(!separatorIterator.hasNext())
              {
                  throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
              }
              stringItem = separatorIterator.next();
              if(separatorIterator.hasNext())
                  throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
              separatorIterator.close();
              if(!stringItem.isString())
                  throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
              try {
                  separator = stringItem.getStringValue();
              } catch (Exception e) {
                  throw new UnexpectedTypeException("Second parameter of tokenize must be a string.", getMetadata());
              }
          }
          _results = input.split(separator);
          _currentPosition = 0;
          if(_children.size() == 1 && _results[0].equals(""))
          {
              _currentPosition++;
          }
          if(_children.size() == 2 && input.matches(".*"+separator+"$"))
          {
              _lastEmptyString = true;
          } else {
              _lastEmptyString = false;
          }
      }
    if(_currentPosition < _results.length)
      {
          _nextResult = new StringItem(_results[_currentPosition]);
          _currentPosition++;
          _hasNext = true;
      } else if (_lastEmptyString) {
          _nextResult = new StringItem(new String(""));
          _hasNext = true;
          _lastEmptyString = false;
      } else {
          _hasNext = false;
      }
    }
    
    private String[] _results;
    private Item _nextResult;
    private int _currentPosition;
    private boolean _lastEmptyString;
}
