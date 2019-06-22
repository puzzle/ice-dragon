# Ice Dragon

[For Conent Providers](content-provider-scripts/README.md)

---

This project has been generated with JHipster. For more details, see [README-jhipster.md](README-jhipster.md).

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

# License / attribution

This project is licensed under the [MIT](LICENSE) license.

The logo is based on [this image](https://www.pngarts.com/explore/119269) licensed under CC 4.0 BY-NC.
