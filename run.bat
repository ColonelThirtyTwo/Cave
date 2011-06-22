@ECHO OFF
java -cp ./build/classes/;lwjgl-2.7.1/jar/lwjgl.jar;lwjgl-2.7.1/jar/lwjgl_util.jar;lwjgl-2.7.1/jar/jinput.jar;lwjgl-2.7.1/jar/slick-util.jar -Djava.library.path=lwjgl-2.7.1/native/windows cave2.Main
pause