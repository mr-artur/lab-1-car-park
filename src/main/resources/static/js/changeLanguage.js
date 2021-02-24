const languageButtons = document.getElementsByClassName("btn-language");
const LANGUAGE_PARAMETER_CODE = 'lang';
const PARAMETERS_DELIMITER = '&';

const changeLanguage = e => {
    const { target: { value } } = e;
    const { location : { pathname, search } } = window;
    window.location.replace(buildNewLocation(pathname, search, value));
}

const buildNewLocation = (pathname, search, value) => {
    if (search.length === 0) {
        return buildLocationWithLang(pathname, value);
    }
    return buildLocationWithManyParameters(pathname, search, value);
}

const buildLocationWithLang = (pathname, value) => {
    return pathname + "?" + LANGUAGE_PARAMETER_CODE + "=" + value;
}

const buildLocationWithManyParameters = (pathname, search, value) => {
    const params = search.substring(1)
        .split(PARAMETERS_DELIMITER)
        .filter(param => !param.includes(LANGUAGE_PARAMETER_CODE));
    params.push(LANGUAGE_PARAMETER_CODE + '=' + value);
    return pathname + '?' + params.join(PARAMETERS_DELIMITER);
}

if (languageButtons) {
    for (let i = 0; i < languageButtons.length; i++) {
        languageButtons[i].addEventListener("click", changeLanguage);
    }
}
