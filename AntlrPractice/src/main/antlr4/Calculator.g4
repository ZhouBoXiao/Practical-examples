grammar Calculator;

LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
CARET: '^';
STAR: '*';
SLASH: '/';
PLUS: '+';
MINUS: '-';
NUMBER: [0-9]+;
WHITESPACE: [ \r\n\t]+ -> skip;

start : expression;

expression
   : NUMBER                                                 # Number
   | MINUS right=expression                                 # Negation
   | LEFT_PARENTHESIS inner=expression RIGHT_PARENTHESIS    # Parentheses
   | left=expression operator=CARET right=expression        # Power
   | left=expression operator=(STAR|SLASH) right=expression # MultiplicationOrDivision
   | left=expression operator=(PLUS|MINUS) right=expression # AdditionOrSubtraction
   ;

//stat: expr NEWLINE         #PrintExpr
//    | ID '=' expr NEWLINE  #Assign
//    | NEWLINE              #Blank
//    ;
//
//expr: expr op=('#'|'/') expr   #MulDiv
//    | expr op=('+'|'-') expr   #AddSub
//    | INT                      #Int
//    | ID                       #Id
//    | '(' expr ')'             #Parens
//    ;
//
//
//INT : [0-9]+ ;
//NEWLINE : '\r'?'\n' ;
//ID : [a-zA-Z]+ ;
//WS : [ \t]+ -> skip;
//
//ADD : '+' ;
//SUB : '-' ;
//MUL : '*' ;
//DIV : '/' ;
