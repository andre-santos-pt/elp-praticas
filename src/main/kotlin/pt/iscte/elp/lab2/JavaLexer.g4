lexer grammar JavaLexer;

KEYWORD: 'class' | 'static' | 'final' | 'public' | 'private' | 'true' | 'false' | 'return';

PRIMITIVE: 'int' | 'boolean';

ID: [a-zA-Z][a-zA-Z_0-9]*;

NUMBER: [0-9]+;

STRING: '"' ~["\n]* '"';

OPENCURL: '{';
CLOSECURL: '}';
OPENPAREN: '(';
CLOSEPAREN: ')';
TERM: ';';

DOT: '.';
EQUAL: '=';

SPACE: [ \t\r\n]+ -> skip;
