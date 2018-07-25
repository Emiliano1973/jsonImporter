package ie.demo.demost.utils.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ie.demo.demost.entities.enums.State;

import java.io.IOException;

/*
This class managing the state deserialion of the State field from jason
 */
public class JSONStateDeserializer extends JsonDeserializer<State> {
    @Override
    public State deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        //get the state json value
        String value = p.getText();
        // I convert the json field in Ste enum
        return State.getStateFromString(value);
    }
}
