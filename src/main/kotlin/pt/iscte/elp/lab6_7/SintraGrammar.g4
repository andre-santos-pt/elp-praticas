grammar SintraGrammar;

script: TERM? param* sequence+=instruction* EOF;

param: 'param' id=ID TERM+;

instruction: (assign | print | while | if | break | COMMENT) TERM+;

assign: ID EQUAL expression;

print: 'print' expression;

while: WHILE guard=expression BLOCKOPEN TERM+ sequence+=instruction* BLOCKCLOSE;

if: IF guard=expression BLOCKOPEN TERM+ sequence+=instruction* BLOCKCLOSE TERM*
(ELSE BLOCKOPEN TERM* alternative+=instruction* BLOCKCLOSE)?;

break: BREAK;

expression:
OPEN inner=expression CLOSE
| left=expression operator=OPERATORMUL right=expression
| left=expression operator=(ADD|MINUS) right=expression
| left=expression operator=OPERATORREL right=expression
| id=ID
| value=INT;


IF: 'if';
ELSE: 'else';
WHILE: 'while';
BREAK: 'break';
PRINT: 'print';
PARAM: 'param';

ID: [a-zA-Z]+;

INT: MINUS?[0-9]+;

BLOCKOPEN: '{';
BLOCKCLOSE: '}';

OPEN: '(';
CLOSE: ')';


OPERATORMUL: '*'|'/'|'%';
OPERATORREL: '<'|'>'|'='|'<>';

ADD: '+';
MINUS: '-';

EQUAL: ':=';

TERM:'\n'+;

COMMENT: '#'~[\n]* -> skip;
SPACE: [ \t]+ -> skip;