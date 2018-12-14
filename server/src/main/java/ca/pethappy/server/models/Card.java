package ca.pethappy.server.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigserial")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String number;

    @Column(name = "exp_month")
    private int expMonth;

    @Column(name = "exp_year")
    private int expYear;

    @Column(name = "name_on_card")
    private String nameOnCard;

    @Column
    private int cvv;
}
