package letsplayjson.json;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonParser {
  public static String transcodeJson(String json) {
    final int length = json.length();

    if(length < 2)
      return json;

    final char firstCharacter = json.charAt(0);
    final char lastCharacter = json.charAt(length - 1);

    final boolean isObject = firstCharacter == '{' && lastCharacter == '}';
    final boolean isArray = firstCharacter == '[' && lastCharacter == ']';

    final String insideJson = json.substring(1,length - 2);

    if(isArray) {
      final String array = transcodeArray(insideJson);
      return "Json.arr(" + array + ")";
    }
    else if(isObject) {
      final String object = transcodeObject(insideJson);
      return "Json.obj(" + object + ")";
    }
    else throw new NotJsonException("Json should be object or json");
  }

  private static String transcodeArray(String insideJson) {
    return Stream.of(insideJson.split(",")).map(line -> {
      final int length = line.length();

      if(length < 2)
        throw new NotJsonException("Error paring near " + line);

      final char firstCharacter = line.charAt(0);
      final char lastCharacter = line.charAt(length - 1);
      final boolean isObject = firstCharacter == '{' && lastCharacter == '}';
      final boolean isArray = firstCharacter == '[' && lastCharacter == ']';

      final String inside = line.substring(1,length - 2);

      if(isArray) {
        final String array = transcodeArray(inside);
        return "Json.arr(" + array + ")";
      } else if (isObject) {
        return asJsObject(line);
      } else
        throw new NotJsonException("Inside Json array there should be object or array");
    }).collect(Collectors.joining(","));
  }

  private static String transcodeObject(String insideJson) {
    return Stream.
        of(insideJson.split(",")).
        map(JsonParser::asJsObject).
        collect(Collectors.joining(","));
  }

  private static String asJsObject(String line) {
    final String[] keyValue = line.split(":");
    if (keyValue.length != 2)
      throw new NotJsonException("line " + line + " is not valid json");
    else {
      final String key = keyValue[0];
      final String value = keyValue[1];

      return key + ":" + transcodeJson(value);
    }
  }
}
