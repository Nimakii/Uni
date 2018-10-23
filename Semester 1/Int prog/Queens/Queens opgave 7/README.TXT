Opgave 2:

På JK's maskine:

**********************************************
Solutions for 16 queens
A total of 14772512 solutions were found in 769310 ms = 13 min
**********************************************

På Thomas maskine:

*****************************************

Solutions for 15 queens
A total of 2279184 solutions were found in 170334 ms = 3 min

******************************************

Opgave 7

Vi har lavet en queenCount der tæller op hver gang vi går ind i et positionQueen kald
og en legalCount der tæller op hver gang vi går ind i et legal kald.
Vi fik en bedre ide og lavede tælleren legalTime, som er den akkumalerede tid i hvert legal kald
når vi så kigger på legalTime/duration ser vi at den ligger lige over 0.5, ved "store" værdier af noOfQueens, som ønsket.
