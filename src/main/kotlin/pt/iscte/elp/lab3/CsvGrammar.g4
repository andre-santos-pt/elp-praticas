grammar CsvGrammar;

csv: line ('\n' line)* ;

line: element (SEPARATOR element)*;

element: (BOOLEAN | NUMBER | STRING)?;

SEPARATOR: ','|';';

BOOLEAN: 'true'|'false';

NUMBER: '-'?[0-9]+('.'[0-9]+)?;

STRING: '"'~('"'|'\n')*'"';

SPACE: ' ' -> skip;
