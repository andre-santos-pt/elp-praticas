lexer grammar ExpressionsLexer;

NUMBER: '-'?[0-9]+('.'[0-9]+)?;

OPERATOR: '+'|'*'|'-'|'/'|'^';

OPEN: '(';

CLOSE: ')';

SPACE: ' '+ -> skip;