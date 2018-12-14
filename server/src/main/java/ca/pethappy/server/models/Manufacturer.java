package ca.pethappy.server.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    @GraphQLQuery(name = "id", description = "A manufacturer's id")
    private Long id;

    @NotNull
    @Column(name = "name", length = 100, nullable = false)
    @GraphQLQuery(name = "name", description = "A manufacturer's name")
    private String name;
}
