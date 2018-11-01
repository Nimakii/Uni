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
            if(x<z)     case y<=x<z så m=x
            else x>=z   case y<=z<= x så m skal ikke ændres da m=z i toppen
    else y>=z
        if(x>y)         case x>y>=z så m=y
        else x<=y
            if(x>z)     case z<x<=y så m=x
            else x<=z   case x<=z<=y så m skal ikke ændres da m=z i toppen
        
Det giver næsten ikke mening at benytte metoderne "indsættelse af ekstra print sætninger" eller
"BlueJ's debugger" da koden kun udfører et par sammenligninger og én variabel modifikation.
Man kunne have benyttet "indsættelse af ekstra print sætninger" til at finde ud af hvilken linje
de forskellige kald benytter. 
