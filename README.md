# Persistent development DB

For the default icedragon-db:

```bash
docker run \
  -d \
  -e POSTGRES_USER=icedragon \
  -e POSTGRES_PASSWORD=icedragon \
  -p 5442:5432 \
  --name icedragon-db \
  --restart unless-stopped \
  postgres:11.2
```
