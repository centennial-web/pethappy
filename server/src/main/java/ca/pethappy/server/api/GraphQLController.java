package ca.pethappy.server.api;

import ca.pethappy.server.services.CategoriesService;
import ca.pethappy.server.services.IngredientsService;
import ca.pethappy.server.services.ManufacturersService;
import ca.pethappy.server.services.ProductService;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class GraphQLController {
    private final GraphQL graphQL;

    @Autowired
    public GraphQLController(CategoriesService categoriesService,
                             ManufacturersService manufacturersService,
                             IngredientsService ingredientsService,
                             ProductService productService) {
        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withResolverBuilders(
                        //Resolve by annotations
                        new AnnotatedResolverBuilder())
                .withOperationsFromSingleton(categoriesService, CategoriesService.class)
                .withOperationsFromSingleton(manufacturersService, ManufacturersService.class)
                .withOperationsFromSingleton(ingredientsService, IngredientsService.class)
                .withOperationsFromSingleton(productService, ProductService.class)
                .withValueMapperFactory(new JacksonValueMapperFactory())
                .generate();
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    @GetMapping("/graphiql")
    public String graphiql() {
        return "forward:/graphiql/index.html";
    }

    @ResponseBody
    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> graphql(@RequestBody Map<String, String> request, HttpServletRequest raw) {
        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(request.get("query"))
                .operationName(request.get("operationName"))
                .context(raw)
                .build());
        return executionResult.toSpecification();
    }
}
