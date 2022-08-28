# AVIONAȘELE - PROIECT REȚELE DE CALCULATOARE 

În cadrul disciplinei **Rețele de calculatoare** am realizat, în echipa formată din trei persoane, un proiect ce constă într-o **aplicație de tip client-server**, respectând anumite cerințe obligatorii. Proiectul a fost notat cu punctajul maxim.

*TEHNOLOGIILE UTILIZATE:* <br />
֍ Limbajul de programare – **Java** <br />
֍ Mediul de dezvoltare – **IntelliJ IDEA** <br />


*CERINȚELE PROIECTULUI:* <br />
֍	server-ul are o listă de fișiere de configurare, fiecare fișier corespunzând unei distribuții a trei avioane pe o matrice de 10x10; <br />
֍	o configurație precizează distribuția a trei avioane sub forma: <br />
00A0000000 <br />
1111100020 <br />
0010002020 <br />
011100222B <br />
0000002020 <br />
0000000020 <br />
0003330000 <br />
0000300000 <br />
0033333000 <br />
0000C00000 <br />
unde cifrele indică parti ale aceluiași avion, iar literele corespund capului avionului, un avion având următoarele segmente: <br />
A - cap <br />
11111 - aripi <br />
1 - corp <br />
111 - coadă <br />
֍	server-ul alege în mod aleator o configurație curentă; <br />
֍	clienții se conectează la server identificându-se printr-un nume unic; <br />
֍	un client poate trage pentru a doborî avioanele, indicând linia și coloana în care trage; <br />
֍	un avion este doborât atunci când se trage în capul lui; <br />
֍	server-ul îi vă raspunde cu 0, dacă niciun avion n-a fost atins, cu 1, dacă o parte a unui avion a fost atinsă, sau cu X, dacă avionul a fost atins în cap și doborât; <br />
֍	când un client a reușit să doboare toate avionale, server-ul notifică toți clienții cu numele acestuia și alege altă configurație curentă, jocul reluându-se. <br />
