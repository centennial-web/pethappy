package ca.pethappy.server.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    @GraphQLQuery(name = "id", description = "A category's id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @GraphQLQuery(name = "name", description = "A category's name")
    private String name;
}
