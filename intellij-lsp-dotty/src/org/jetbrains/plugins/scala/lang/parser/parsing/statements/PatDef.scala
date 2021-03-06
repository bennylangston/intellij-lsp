package org.jetbrains.plugins.scala.lang.parser.parsing.statements

import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes
import org.jetbrains.plugins.scala.lang.parser.ScalaElementTypes
import org.jetbrains.plugins.scala.lang.parser.parsing.builder.ScalaPsiBuilder
import org.jetbrains.plugins.scala.lang.parser.parsing.expressions.Expr
import org.jetbrains.plugins.scala.lang.parser.parsing.patterns.Pattern2
import org.jetbrains.plugins.scala.lang.parser.parsing.types.Type
import org.jetbrains.plugins.scala.lang.parser.util.ParserUtils

/**
* @author Alexander Podkhalyuzin
* Date: 06.02.2008
*/

/*
 * PatDef ::= Pattern2 {',' Pattern2} [':' Type] '=' Expr
 */
object PatDef extends PatDef {
  override protected def expr = Expr
  override protected def pattern2 = Pattern2
  override protected def `type` = Type
}

//TODO: Rewrite this
trait PatDef {
  protected def expr: Expr
  protected def pattern2: Pattern2
  protected def `type`: Type

  def parse(builder: ScalaPsiBuilder): Boolean = {
    val someMarker = builder.mark
    val pattern2sMarker = builder.mark

    if (!pattern2.parse(builder, forDef = true)) {
      pattern2sMarker.rollbackTo()
      someMarker.drop()
      return false
    }

    while (ScalaTokenTypes.tCOMMA.equals(builder.getTokenType)) {
      ParserUtils.eatElement(builder, ScalaTokenTypes.tCOMMA)

      if (!pattern2.parse(builder, forDef = true)) {
        pattern2sMarker.rollbackTo()
        someMarker.drop()
        return false
      }
    }

    pattern2sMarker.done(ScalaElementTypes.PATTERN_LIST)

    var hasTypeDcl = false

    if (ScalaTokenTypes.tCOLON.equals(builder.getTokenType)) {
      ParserUtils.eatElement(builder, ScalaTokenTypes.tCOLON)

      if (!`type`.parse(builder)) {
        builder error "type declaration expected"
      }

      hasTypeDcl = true
    }
    if (!ScalaTokenTypes.tASSIGN.equals(builder.getTokenType)) {
      someMarker.rollbackTo()
      false
    } else {
      ParserUtils.eatElement(builder, ScalaTokenTypes.tASSIGN)

      if (!expr.parse(builder)) {
        builder error "expression expected"
      }

      someMarker.drop()
      true
    }
  }
}