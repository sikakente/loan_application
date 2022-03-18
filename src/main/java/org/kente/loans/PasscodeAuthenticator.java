package org.kente.loans;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasscodeAuthenticator {


    public List<Integer> generatePasscodeAuthPositions(String passcode){
        List<Integer> positions = IntStream.rangeClosed(1, passcode.length())
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(positions);

        return positions.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public boolean authenticate(HashMap<Integer, String> userInput, String passcode){
        for(Integer position: userInput.keySet()){
            userInput.get(position);
            if(!userInput.get(position).equals(Character.toString(passcode.charAt(position-1)))) return false;
        }

        return true;
    }
}
