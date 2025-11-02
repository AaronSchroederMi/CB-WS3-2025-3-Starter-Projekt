grammar Blatt3Grammatik;

//Parser
start: statement* EOF;

statement   : assign
            | expresion
            | conditional
            | loop
            ;

assign      : ID ':=' expresion ;

expresion   : arithmetic
            | STRING
            ;

arithmetic  : arithmetic '*' arithmetic
            | arithmetic '/' arithmetic
            | arithmetic '+' arithmetic
            | arithmetic '-' arithmetic
            | term
            ;

condition   : condition '==' condition
            | condition '!=' condition
            | condition '>' condition
            | condition '<' condition
            | term | STRING
            ;

conditional : 'if' condition 'do' statement* 'end'
            | 'if' condition 'do' statement* 'else do' statement* 'end'
            ;

loop        : 'while' condition 'do' statement* 'end'
            ;

term        : NUMBER | ID ;


//Lexer

ID: [a-zA-Z_][a-zA-Z0-9_]* ;

NUMBER: [0-9]+ ;
STRING: '"' (~[\n\r"])* '"';

COMMENT: '#' (~[\n])* -> skip;
WS    : [ \t\n]+ -> skip ;
