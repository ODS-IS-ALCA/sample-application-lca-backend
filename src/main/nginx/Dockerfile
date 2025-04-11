FROM 673913797649.dkr.ecr.ap-northeast-1.amazonaws.com/ecr-public/nginx/nginx:1.25

USER 0

COPY ["server.conf.template", "/tmp/server.conf.template"]

EXPOSE 80

ENTRYPOINT ["/bin/sh", "-c"]
CMD ["export RESOLVER=$(cat /etc/resolv.conf | grep nameserver | awk '{print $2}') && envsubst '$$RESOLVER $$URL_COMMON_FRONTEND $$URL_COMMON_BACKEND' < /tmp/server.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]
