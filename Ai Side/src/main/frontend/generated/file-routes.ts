import type { AgnosticRoute } from "@vaadin/hilla-file-router/types.js";
import { createRoute } from "@vaadin/hilla-file-router/runtime.js";
import * as Page0 from "../views/@index.js";
import * as Page1 from "../views/chat.js";
import * as Layout2 from "../views/@layout.js";
const routes: readonly AgnosticRoute[] = [
    createRoute("", Layout2, [
        createRoute("", Page0),
        createRoute("chat", Page1)
    ])
];
export default routes;
