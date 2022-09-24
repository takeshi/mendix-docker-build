docker run -it \
  -e ADMIN_PASSWORD=1! \
  -e DATABASE_ENDPOINT=postgres://postgres:postgres@localhost:5433/mendix \
  mymendix:0.0.1