grammar IdList;

list: element (COMMA element)*;

element: ID;

ID: [a-zA-Z]+;

COMMA: ',';

SPACE: [ \t\r\n]+ -> skip;