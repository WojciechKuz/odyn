
:: Ten skrypt usuwa z git'a zbędne katalogi i pliki projektu Android Studio

:: katalogi:	git rm -r --cached <folder>
:: pliki:	git rm --cached <file>

git rm -r --cached app/build

git rm -r --cached .gradle/
git rm -r --cached build/

git rm --cached local.properties

git rm --cached *.log

git rm -r --cached captures/
git rm -r --cached .externalNativeBuild/
git rm -r --cached .cxx/
git rm --cached *.apk
git rm --cached output.json

git rm --cached *.iml
git rm -r --cached .idea/
git rm --cached misc.xml
git rm --cached deploymentTargetDropDown.xml
git rm --cached render.experimental.xml

git rm --cached *.jks
git rm --cached *.keystore

git rm --cached *.hprof