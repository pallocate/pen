package pen.tests.eco.common

import java.io.ByteArrayInputStream
import org.junit.jupiter.api.*
import pen.eco.common.IniStreamParser

@DisplayName( "IniParser Test" )
class IniParserTest
{
   val validInput =
   """
      [HEADER]
      hstring:foo
      hinteger:1
      hfloat:1.1
      [ITEM]
      istring:bar
      [ITEM]
      istring:baz
   """.toByteArray()

   val badInput1 =
   """
      [HEADER]
      hfloat:>"'´|
   """.toByteArray()

   val badInput2 =
   """
      [HEADER
      :hstring:[ITEM][HEADER]]
      :
      [ITEM]
   """.toByteArray()

   class TestParser : IniStreamParser
   {
      override val HEADER_KEY                      = "HEADER"
      override val ITEM_KEY                        = "ITEM"

      var headerString = ""
      var headerInteger = 0
      var headerFloat = 0F
      var itemString = ""

      override fun headerSection (map : HashMap<String, String>)
      {
         headerString = map.getOrElse("hstring", { "" })
         headerInteger = map.getOrElse("hinteger", { "0" }).toInt()
         headerFloat = map.getOrElse("hfloat", { "0.0" }).toFloat()
      }

      override fun itemSection (map : HashMap<String, String>)
      {
         itemString = map.getOrElse("istring", { "" })
      }
   }

   var testParser = TestParser()

   @BeforeEach
   fun init ()
   { testParser = TestParser() }


   @Test
   @DisplayName( "Using bad input" )
   fun usingBadInput ()
   {
      /* Should fail due to number parsing exception */
      Assertions.assertTrue( !testParser.read(ByteArrayInputStream( badInput1 )) )

      /* Should not result in any valid parsing */
      Assertions.assertTrue( testParser.read(ByteArrayInputStream( badInput2 )) )
      Assertions.assertEquals( "", testParser.headerString )
      Assertions.assertEquals( 0, testParser.headerInteger )
      Assertions.assertEquals( 0F, testParser.headerFloat )
      Assertions.assertEquals( "", testParser.itemString )
   }

   @Test
   @DisplayName( "Using valid input" )
   fun usingValidInput ()
   {
      Assertions.assertTrue( testParser.read(ByteArrayInputStream( validInput )) )
      Assertions.assertEquals( "foo", testParser.headerString )
      Assertions.assertEquals( 1, testParser.headerInteger )
      Assertions.assertEquals( 1.1F, testParser.headerFloat )
      Assertions.assertEquals( "baz", testParser.itemString )
   }
}
