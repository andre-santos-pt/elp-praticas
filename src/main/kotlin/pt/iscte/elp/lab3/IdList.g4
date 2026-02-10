grammar IdList;

list: id (COMMA id)*;

id: ID;

ID: [a-zA-Z]+;

COMMA: ',';

SPACE: [ \t\r\n]+ -> skip;