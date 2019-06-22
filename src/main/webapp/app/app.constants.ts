// These constants are injected via webpack environment variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

export const VERSION = process.env.VERSION;
export const DEBUG_INFO_ENABLED: boolean = !!process.env.DEBUG_INFO_ENABLED;
export const SERVER_API_URL = process.env.SERVER_API_URL;

export function getServerUrl() {
  return SERVER_API_URL;
}

export function getWebsocketUrl($window: any) {
  let url = '';
  // if there is only a path configured, we
  if (getServerUrl().startsWith('/')) {
    // building absolute path so that websocket doesn't fail when deploying with a context path
    const loc = $window.nativeWindow.location;
    url = '//' + loc.host + getServerUrl() + 'websocket/';
  } else {
    let serverUrl = getServerUrl();
    serverUrl = serverUrl.replace(/https:\/\//gi, '');
    serverUrl = serverUrl.replace(/http:\/\//gi, '');
    url = '//' + serverUrl + 'websocket/';
  }

  return url;
}
