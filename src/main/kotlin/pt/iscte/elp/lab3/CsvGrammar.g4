grammar CsvGrammar;

csv: line (NEWLINE line)* ;

line: value (SEPARATOR value)*;

value: (BOOLEAN | NUMBER | STRING)?;

SEPARATOR: ','|';';

BOOLEAN: 'true'|'false';

NUMBER: '-'?[0-9]+('.'[0-9]+)?;

STRING: '"'~('"'|'\n')*'"';

NEWLINE: '\n'|'\r\n';

SPACE: (' '|'\t') -> skip;
