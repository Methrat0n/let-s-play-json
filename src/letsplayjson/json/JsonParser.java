package letsplayjson.json;

import java.util.stream.Stream;

public class JsonParser {
  public static boolean isJson(String maybeJson) {
    return isJsonObject(maybeJson) || isJsonArray(maybeJson);
  }

  /**
   * Test if a given string is a valid json object
   * for one level. It does this by validating the bracket around the string
   * and checking if the string compose of "key": "value" whatever the value may be
   */
  private static boolean isJsonObject(String maybeJsonObject) {
    final int length = maybeJsonObject.length();

    if(length < 2) //minimum json is "{}"
      return false;

    final char firstCharacter = maybeJsonObject.charAt(0);
    final char lastCharacter = maybeJsonObject.charAt(length - 1);

    final boolean asBrackets = firstCharacter == '{' && lastCharacter == '}';
    if(length == 2 && asBrackets)
      return true;

    final String inBrackets = maybeJsonObject.substring(1,length - 2);
    return Stream.of(inBrackets.split(","))
        .allMatch(inBracket -> inBracket.split(":").length == 2);
  }

  /**
   * Test if a given string is a valid json array
   * for one level. It does this by validating the [] around it and
   * checking if the subelement are made of [] or {} separated by commas
   */
  private static boolean isJsonArray(String maybeJsonArray) {
    final int length = maybeJsonArray.length();

    if(length < 2) //minimum json is "[]"
      return false;

    final char firstCharacter = maybeJsonArray.charAt(0);
    final char lastCharacter = maybeJsonArray.charAt(length - 1);

    final boolean asBrackets = firstCharacter == '[' && lastCharacter == ']';
    if(length == 2 && asBrackets)
      return true;

    final String inBrackets = maybeJsonArray.substring(1,length - 2);
    return Stream.of(inBrackets.split(","))
        .allMatch(inBracket -> inBracket.matches("^(\\[.*\\])|(\\{.*\\})$"));
  }


  public static String transcodeJson(String json) throws NotJsonException {
    throw new UnsupportedOperationException("not yet");
  }

}
