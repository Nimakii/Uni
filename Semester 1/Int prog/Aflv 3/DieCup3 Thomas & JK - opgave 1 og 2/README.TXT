Details about this project are descibed in the
DieCup 1, DieCup 2 and DieCup 3 exercises

DieCup 2 opgave 2
Vi viste i DieCup 1 at gennemsnittet af 2 seks sidede terninger er 7,
s� vi ser at vores computer simulation passer rimelig godt med det matematiske resultat.

DieCup 2 opave 6
Vi husker at middelv�rdien af slag med 2 d6 er 7
Ligeledes er middelv�rdierne E[d4]=2.5 og E[d8]=4.5,
og da vi sl�r vores to terninger uafh�ngigt af hinanden
bliver E[d4+d8]=E[d4]+E[d8]=7
S� middelv�rdien af slagene i de to rafleb�gere er ens.
Vi kigger p� variansen
V[X] = E[X^2]-E[X]^2
E[(dn)^2] = sum_{x=1..n} x^2 P(dn = x)
Bem�rk at P(dn = x) = 1/n
E[(dn)^2] = (sum_{x=1..n} x^2)/n = 1/6 * n (n+1)(2n+1)/n
          = (n+1)(2n+1)/6
E[dn] = sum_{x=1..n} x P(dn = x)
      = 1/n sum_{x=1..b} x
      = 1/n (n(n+1)/2)
      = (n+1)/2
S�
V[dn] = (n+1)(2n+1)/6 - ((n+1)/2)^2
Vi kan nu anvende vores formel
V[d6] = 7*13/6 - (7/2)^2
      = 35/12
V[d4] = 5*9/6 - (5/2)^2
      = 5/4
V[d8] = 9*17/6 - (9/2)^2
      = 21/4
Husk at variansen af en sum af stokastiske variable er summen af varianser,
hvis og kun hvis de stokastiske variable er uafh�ngige, alts�
V[2d6] = V[d6+d6] = V[d6]+V[d6] = 70/12
V[d4+d8] = V[d4]+V[d8] = 26/4 = 78/12
Vi kan da konkludere at variansen er lavere for de to sekssidede terninger,
alts� at middelv�rdien 7 forekommer oftere for disse. Dog vil denne forskel
udlignes ved store antal kast. S� vi kan konkludere at ved sm� antal slag
forventer vi lidt f�rre tilf�lde hvor begge rafleb�gere har sl�et 7, end hvis
begge rafleb�gere havde haft 2d6.

Dvs. at ved sm� antal kast kan det forekomme at 2d6 vinder stort, eller taber
stort pga. den st�rre varians p� d4+d8, men da middelv�rdien er den samme vil
dette udligne sig ved st�rre m�ngde kast. 