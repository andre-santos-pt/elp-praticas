grammar ExpressionsGrammar;

exp
    : value=NUMBER
    | OPEN expression=exp CLOSE
    | left=exp multOperator right=exp
    | left=exp addOperator right=exp
;

multOperator:
    MULT | DIV | POW;

addOperator:
    PLUS | MINUS;

NUMBER: [0-9]+('.'[0-9]+)?;
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
POW: '^';

OPEN: '(';
CLOSE: ')';
SPACE: ' '+ -> skip;
