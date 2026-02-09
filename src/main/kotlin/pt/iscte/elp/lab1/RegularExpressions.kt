package pt.iscte.elp.lab1

val binary = Regex("[01]+")

val accessModifier = Regex("public|private|protected")

val javaIdentifier = Regex("[a-zA-Z_][a-zA-Z0-9_]")

val xmlTag = Regex("</?[a-zA-Z]+>")

val number = Regex("-?[0-9]+(\\.[0-9]+)?")

val date = Regex("\\d{2}-\\d{2}-\\d{4}")

val phoneNumber = Regex("\\(\\+\\d{1,3}\\)\\s*\\d{9}")

val string = Regex("\"[^\"]*\"")

val email = Regex("[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)* ")

val positiveIntList = Regex("[0-9]+(\\s*,\\s*[0-9]+)*")

val jsonNumberArray = Regex("\\[((${number.pattern})?|(${number.pattern}(,${number.pattern})+))]")


