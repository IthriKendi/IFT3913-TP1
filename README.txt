# IFT3913-TP1
Auteurs:
Walid Boudehane (20206664)
Ithri Kendi     (20222832)

Lien vers le github:
https://github.com/IthriKendi/IFT3913-TP1

# Structure du projet
Nos fichiers de code sont dans:
tp1/src/main/java/ift3913

Nous avons une classe pour chaque fonction donc:
tloc.java
tassert.java
tls.java
tropcomp.java

# Compilation
Pour compiler le code il faut se rendre dans le dossier tp1/src/main/java/ et faire cette commande dans le terminal pour compiler toutes les classes en meme temps:
javac .\ift3913\*.java

Ou se rendre dans le dossier tp1/src/main/java/ift3913 et compiler chaque classe a part en faisant cette commande:
javac <nom-de-la-classe>.java

# Execution des ficher source
Pour executer les fonctions, se rendre dans le fichier tp1/src/main/java/ift3913 et faire ces commandes pour chaque classe:

tloc :      java tloc.java <chemin-du-ficher-test>

tassert :   java tassert.java <chemin-du-ficher-test>

tls :       java tls.java <chemin-de-l'entrée>
        ou  java tls.java -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée> 
        pour avoir la sortie dans un ficher

tropcomp :  java tropcomp.java <chemin-de-l'entrée> <seuil>
        ou  java tropcomp.java -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée> <seuil>
        pour avoir la sortie dans un ficher

# Execution des fichers JAR
Pour executer les fichiers JAR, se rendre dans le fichier qui contient les JARs et faire ces commandes pour chaque classe:

tloc :      java -jar tloc.jar <chemin-du-ficher-test>

tassert :   java -jar tassert.jar <chemin-du-ficher-test>

tls :       java -jar tls.jar <chemin-de-l'entrée>
        ou  java -jar tls.jar -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée> 
        pour avoir la sortie dans un ficher

tropcomp :  java -jar tropcomp.jar <chemin-de-l'entrée> <seuil>
        ou  java -jar tropcomp.jar -o <chemin-à-la-sortie.csv> <chemin-de-l'entrée> <seuil>
        pour avoir la sortie dans un ficher


