Opgave 1

    Vi udfører en række test for at komme fejlen nærmere:
    test123 giver resultatet 2, denne test tester tilfældet x<y<z
    test132 giver resultatet 2, denne test tester tilfældet x<z<y
    test213 giver resultatet 1, denne test tester tilfældet y<x<z
    test231 giver resultatet 2, denne test tester tilfældet z<x<y
    test312 giver resultatet 2, denne test tester tilfældet z<x<y
    test321 giver resultatet 2, denne test tester tilfældet z<y<z
    
    Efter at have observeret fejlen ved medianWrong(2,1,3) modificerer vi vores hypotese.   
    
    Hypotese:    medianWrong giver det forkerte resultat med input af typen y<x<z
    eksperiemnt: medianWrong(2,1,3) med forventet resultat 2, 
                 medianWrong(8,3,9) med forventet resultat 8
    resultat:    medianWrong(2,1,3)=1!=2 og medianWrong(8,1,9)=3!=8
    Hypotesen er korrekt. Idet hypotesen er korrekt og vores eksperimenter har bekræftet fejlen
    retter vi fejlen i metoden medianCorrect(). Vi lokalisere at fejlen ligger i linje 18 da
    vi her er i tilfældet y<=x<z jævnfør den manuelle gennemgang.

Manuel gennemgang af koden:
    Vi kigger i øverste del af koden og ser at vi i tilfældet y<=x<z sætter medianen
    til at være y, og ikke x som den burde.
    Vi gennemgår cases:
    if(y<z)
        if(x<y)         case x<y<z og m=y
        else x>=y
            if(x<z)     case y<=x<z så m=x <--- her bor fejlen i koden
            else x>=z   case y<=z<= x så m skal ikke ændres da m=z i toppen
    else y>=z
        if(x>y)         case x>y>=z så m=y
        else x<=y
            if(x>z)     case z<x<=y så m=x
            else x<=z   case x<=z<=y så m skal ikke ændres da m=z i toppen
        
Indsættelse af printsætninger:
    Ved metodekald Median.medianWrongPrintTest(2,1,3) får vi printet
    The error is in line 56
    Så vi kigger i linje 56 og ser som før at det er her fejlen ligger, denne anvendelse
    viser imidlertid ikke at det er præcis her fejlen er, men at det er når vi ender her
    at der er sket fejl undervejs. I vort tilfælde er det imidlertid i den seneste
    kodestreng m=y det går galt

Denne måde at bruge printsætninger på giver et fint overblik over hvor i koden der er bøvl
hvormed vi kan afgrænse vores fejfinding til at kigge i "strengen" af kode der får os ud i
det sted der bliver printet. Herefter er en manuel gennemgang stadig nødvendig, men
begrænset i omfang i forhold til at læse hele koden igennem. Vi er dog på ingen måde sikret
imod at der så ikke kommer fejl et andet sted, men den tid den sorg.