grammar MyLangMinus;

start
  : stmt* EOF
  ;

stmt
  : ID '=' expr ';'
  | expr ';'
  ;

expr
  : expr '*' expr
  | expr '+' expr
  | ID
  | NUM
  ;


ID  : [a-zA-Z_] [a-zA-Z_0-9]* ;
NUM : [0-9]+ ;

WS  : [ \t\r\n]+ -> skip ;



/*

result99 = acc_2*ACC_2 + spillover7 + bonus_1*3 + inc_0;
calc_42 = __9 * zZ_1 + 5 + FooBar_42 * bar_7 + q0;
_ExprLine + A_1 * bB_2 + cc3 * 7 +      11;

*/
