package ca.pethappy.server.services;

import ca.pethappy.server.models.Manufacturer;
import ca.pethappy.server.repositories.ManufacturersRepo;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ManufacturersService implements CrudService<Manufacturer, Long> {
    private final ManufacturersRepo manufacturersRepo;

    @Autowired
    public ManufacturersService(ManufacturersRepo manufacturersRepo) {
        this.manufacturersRepo = manufacturersRepo;
    }

    @Override
    @Cacheable("manufacturers")
    @GraphQLQuery(name = "manufacturers")
    @Transactional(readOnly = true)
    public List<Manufacturer> findAll() {
        return manufacturersRepo.findAll();
    }

    @Override
    @GraphQLQuery(name = "manufacturer")
    @Transactional(readOnly = true)
    public Optional<Manufacturer> findById(@GraphQLArgument(name = "id") Long id) {
        return manufacturersRepo.findById(id);
    }

    @Override
    @GraphQLMutation(name = "saveManufacturer")
    public Manufacturer save(@GraphQLArgument(name = "manufacturer") Manufacturer manufacturer) {
        return manufacturersRepo.save(manufacturer);
    }

    @Override
    @GraphQLMutation(name = "deleteManufacturer")
    public void delete(@GraphQLArgument(name = "id") Long id) {
        manufacturersRepo.deleteById(id);
    }
}
