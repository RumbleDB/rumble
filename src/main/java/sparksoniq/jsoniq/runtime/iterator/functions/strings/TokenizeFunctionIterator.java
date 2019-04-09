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
        _input = null;
        setNextResult();
    }

    public void setNextResult() {
      if(_input == null)
      {
        RuntimeIterator stringIterator = _children.get(0);
        stringIterator.open(_currentDynamicContext);
        if(!stringIterator.hasNext())
        {
            _nextResult= null;
            _hasNext = false;
            stringIterator.close();
            return;
        }
        Item stringItem = stringIterator.next();
        if(!stringItem.isString())
            throw new UnexpectedTypeException("First parameter of tokenize must be a string or the empty sequence.", getMetadata());
        try {
            _input = stringItem.getStringValue();
            _position1 = 0;
            _position2 = 0;
        } catch (Exception e) {
            throw new UnexpectedTypeException("First parameter of tokenize must be a string or the empty sequence.", getMetadata());
        }
      }
      char c = 0;
      if(_position2 < _input.length())
      {
          c = _input.charAt(_position2);
      }
      while(_position2 < _input.length() && (c == ' ' || c == '\t' || c == '\n' || c == '\r'))
      {
        ++_position2;
        if(_position2 < _input.length())
        {
            c = _input.charAt(_position2);
        }
      }
      // here we know that _position2 is either a non-whitespace or EOB
      _position1 = _position2;
      // here we know that _position1 == _position1 and is either a non-whitespace or EOB

      // here we know that _position1 == _position2
      // here we know that they are either a non-whitespace or EOB
      if(_position1 == _input.length())
      {
          _nextResult = null;
          _hasNext = false;
          return;
      }
      // here we know that _position1 and _position2 point to a char

      while(_position2 < _input.length() && !(c == ' ' || c == '\t' || c == '\n' || c == '\r'))
      {
        ++_position2;
        if(_position2 < _input.length())
        {
            c = _input.charAt(_position2);
        }
      }
      // here we know that _position2 is either a whitespace or EOB
      if(_position2 < _input.length())
      {
         // if _position2 is EOB
        _nextResult = new StringItem(_input.substring(_position1, _position2));
      } else
      {
        // if _position2 is a whitespace
        _nextResult = new StringItem(_input.substring(_position1));
      }
      _hasNext = true;

    }
    
    private String _input;
    private Item _nextResult;
    private int _position1;           // current position
    private int _position2;           // current position
}
