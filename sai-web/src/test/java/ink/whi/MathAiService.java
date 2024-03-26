package ink.whi;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: qing
 * @date: 2024/8/13
 */
@Data
class Step implements Serializable {
    String explanation;
    String output;
}

@Data
class MathReasoning implements Serializable {
    @Serial
    private static final long serialVersionUID = 6826192233149267216L;

    Step[] steps;
    String finalAnswer;
}

interface MathProblem {

    @dev.langchain4j.service.SystemMessage("You are a helpful math tutor. Guide the user through the solution step by step solve the problem {{it}}")
    MathReasoning calculator(String problem);
}

public class MathAiService {

    public static void main(String[] args) {
        String baseUrl = "https://api.chatanywhere.tech";
        String apiKay = "sk-6d1s3M2cPvSCb6byweSI5GdekMo0FF87u3Cg1nhofwQyq2fA";
        OpenAiChatModel model = OpenAiChatModel
                .builder()
                .apiKey(apiKay)
                .baseUrl(baseUrl)
                .modelName("gpt-4o-2024-08-06")
                .responseFormat("json_object")
                .build();

        MathProblem mathProblem = AiServices.create(MathProblem.class, model);
        MathReasoning calculator = mathProblem.calculator("how can I solve 8x + 7 = -23");
        System.out.println(calculator);
    }
}