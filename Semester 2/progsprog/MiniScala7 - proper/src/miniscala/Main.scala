package miniscala

import miniscala.Ast.MiniScalaError
import miniscala.parser.{Files, Parser}

object Main {

  /**
    * The main entry of MiniScala.
    */
  def main(args: Array[String]): Unit = {
    try {
      // read the command-line arguments
      Options.read(args)

      // parse the program
      val program = Parser.parse(Parser.readFile(Options.file))

      // load and execute abstract machine code, if enabled
      if (Options.machine) {
        val bin = Files.load(Options.file)
        println(s"Executable (symbolic form): $bin")
        val initialEnv = AbstractMachine.makeInitialEnv(bin)
        val result = AbstractMachine.execute(bin, initialEnv)
        println(s"Output: $result")

      }

      // type check the program, if enabled
      if (Options.types) {
        val initialTypeEnv = TypeChecker.makeInitialTypeEnv(program)
        TypeChecker.typeCheck(program, initialTypeEnv)
      }

      // execute the program, if enabled
      if (Options.run) {
        val initialEnv = Interpreter.makeInitialEnv(program)
        val (result, _) = Interpreter.eval(program, initialEnv, Map())
        println(s"Output: ${Interpreter.valueToString(result)}")
      }

    } catch { // report all errors to the console
      case e: Options.OptionsError =>
        println(e.getMessage)
        println(Options.usage)
      case e: MiniScalaError =>
        println(e.getMessage)
    }
  }

}
