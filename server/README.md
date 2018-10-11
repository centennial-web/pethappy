### Create a network
`
docker network create --driver bridge petnetwork
`

### Create Postgres container
`
docker run --name petpg --network petnetwork -p 5432:5432 -v petpg-volume:/var/lib/postgresql/data -e POSTGRES_PASSWORD=Vls021130 -d postgres
`

### Create PGAdmin container
`
docker run -v petpgadmin-volume:/var/lib/pgadmin -p 8095:80 --name petpgadmin --network petnetwork -e "PGADMIN_DEFAULT_EMAIL=admin@pethappy.com" -e "PGADMIN_DEFAULT_PASSWORD=Vls021130" -d dpage/pgadmin4
`

### Use PGAdmin to create the database
- Navigate to: `http://localhost:8095`
- User/Password: `admin@pethappy.com / Vls021130`
- Right click `Servers > Create > Server...`
  - General/Name: `pethappy`
  - Connection/Host name/address: `petpg`
  - Port: `5432`
  - Maintance database: `postgres`
  - Username: `postgres`
  - Password: `Vls021130`
  - Click `Save`
- Right click `Servers > pethappy > Databases > Create > Database...`
  - General/Database: `pethappy`
  - Definition/Collation: `en_US.utf8`
  - Definition/Character Type: `en_US.utf8`
  - Click `Save`