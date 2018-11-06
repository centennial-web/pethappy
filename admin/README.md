## Frontend

### Dependencias

- yarn add axios
- yarn add bluebird
- yarn add immutability-helper
- yarn add lodash
- yarn add prop-types
- yarn add react-redux
- yarn add redux
- yarn add redux-promise
- yarn add redux-actions
- yarn add redux-thunk-fsa
- yarn add normalize.css

### Configurando o prettier (formatacao automatica de codigo quando salva)

- yarn add prettier --dev --exact
- yarn add --dev eslint-plugin-prettier
- Instalar a extensao do ESLint (https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)
- Instalar a extensao do Prettier (https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode)
- Cria arquivo .eslintrc

```json
{
  "extends": "react-app",
  "plugins": ["prettier"],
  "rules": {
    "prettier/prettier": "error"
  }
}
```

- Criar arquivo .prettierrc

```json
{
  "singleQuote": true,
  "trailingComma": "es5"
}
```

- Alterar a workspace settings:
  ```json
  "editor.formatOnSave": true,
  "editor.tabSize": 2
  ```

### Outros

- Criar arquivo `.env` para dizer qual eh o NODE_PATH: `NODE_PATH=src/`
- Configurar bluebird como promise padrao. Criar arquivo `src/polyfill.js`

```javascript
import Promise from 'bluebird';

// channel default promises to bluebird.
window.Promise = Promise;
```

- Importar no `src/index.js`:

```javascript
import './polyfill';
```
