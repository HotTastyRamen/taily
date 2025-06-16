const createTarget = (port) => ({
  target: `http://localhost:${port}`,
  secure: false,
  changeOrigin: false,
  logLevel: 'debug',
});

module.exports = {
  '/auth': createTarget(8080),
  '/loginCookie': createTarget(8080),
  '/user': createTarget(8080),
  '/admin': createTarget(8080),
  '/documents': createTarget(8085),
  '/signatures': createTarget(8085),
  '/getVerToken': createTarget(8084),
  '/document-versions': createTarget(8085),
  '/confirmSignatureCode' : createTarget(8084),
};
