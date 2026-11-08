# TP 07 — Réponses aux questions de compréhension

## 1. `@Controller` vs `@RestController`

- `@Controller` marque une classe dont les méthodes renvoient généralement un **nom de vue** (`String`). Le *View Resolver* le traduit en template (`"helloworld"` → `templates/helloworld.html`), qui est rendu en HTML avec les données du `Model`. Il convient aux applications web **côté serveur** (pages HTML, formulaires).
- `@RestController` est `@Controller` + `@ResponseBody` : la valeur retournée est **écrite directement dans le corps de la réponse HTTP** (le plus souvent en JSON), sans passer par une vue. Il convient aux **API REST** consommées par un front JavaScript, une application mobile ou un autre service.

Dans ce TP, on veut des pages HTML générées par Thymeleaf : on utilise `@Controller`.

## 2. Rôle du `DispatcherServlet` et du View Resolver pour `/showStudentForm`

1. Le navigateur envoie `GET /showStudentForm`.
2. Le **`DispatcherServlet`** (front controller unique) reçoit la requête et cherche, grâce aux mappings, la méthode qui la gère : `StudentController.showForm(...)` (`@GetMapping("/showStudentForm")`).
3. Le contrôleur crée un `Student` vide, place `student`, `countries`, `languages` et `systems` dans le `Model`, puis retourne `"student-form"`.
4. Le **View Resolver** (Thymeleaf) traduit ce nom logique en chemin physique `src/main/resources/templates/student-form.html`.
5. Thymeleaf évalue les attributs `th:*` avec les données du `Model` et produit du HTML pur, renvoyé au navigateur.

## 3. Les trois façons de lire un formulaire

| Méthode | Principe | Usage |
|---|---|---|
| `HttpServletRequest.getParameter("x")` | Accès direct à l'API Servlet, valeur toujours `String`, conversion manuelle | Rare : compatibilité ou besoin de l'objet requête complet |
| `@RequestParam("x") String x` | Spring injecte directement le paramètre, avec conversion de type automatique | Peu de paramètres simples |
| `@ModelAttribute("s") Student s` | Spring crée l'objet, appelle ses setters avec les paramètres correspondants (liaison de données) | Formulaires à plusieurs champs |

`@ModelAttribute` est recommandé pour un formulaire à plusieurs champs car : le code reste court (un seul paramètre au lieu d'une dizaine), les types sont convertis automatiquement (y compris les `List<String>` des cases à cocher), l'objet métier est réutilisable tel quel (validation, service, base de données), et l'ajout d'un champ ne modifie pas la signature de la méthode.

## 4. `${...}`, `*{...}` et `@{...}`

- **`${...}` (variable)** : évalue une variable du modèle, depuis n'importe où dans la page.
  Exemple : `<span th:text="${student.country}"></span>`
- **`*{...}` (sélection)** : évalue une propriété **relative à l'objet** défini par `th:object`.
  Exemple : `<form th:object="${student}"> <input th:field="*{firstName}" />` (équivalent à `student.firstName`)
- **`@{...}` (URL)** : construit une URL gérée par Thymeleaf (ajoute automatiquement le *context-path*).
  Exemple : `<form th:action="@{/processStudentForm}">` ou `<link th:href="@{/css/demo.css}">`

## 5. GET pour afficher, POST pour soumettre

- **GET** est destiné à *lire* une ressource : il est sûr et idempotent, les paramètres apparaissent dans l'URL (historique, logs, partage, longueur limitée). Il convient donc à l'affichage d'un formulaire vide.
- **POST** est destiné à *envoyer* des données qui déclenchent une action : les données sont dans le corps de la requête (pas dans l'URL), sans limite pratique de taille, et la sémantique est celle d'une opération qui modifie quelque chose.

Si on rafraîchit (F5) une page obtenue après un POST, le navigateur affiche l'avertissement « voulez-vous renvoyer les données du formulaire ? » : en confirmant, la **requête POST est rejouée**, donc l'action est ré-exécutée (double inscription, double paiement…). Le patron *Post/Redirect/Get* permet d'éviter ce problème.

## 6. Ce que génère `th:field="*{firstName}"`

Sur un `<input type="text">`, Thymeleaf génère :

```html
<input type="text" id="firstName" name="firstName" value="" />
```

(`value` contient la valeur actuelle de `student.firstName` si elle existe.) C'est pratique car :
- `name` correspond exactement au nom de la propriété, ce qui permet la liaison automatique à la soumission ;
- `id` est cohérent (utile pour les `<label>`) ;
- `value` pré-remplit le champ depuis l'objet (valeurs par défaut, ré-affichage) ;
- on écrit une seule expression au lieu de trois attributs, ce qui évite les fautes de frappe.

## 7. Intérêt d'externaliser les listes dans `application.properties`

- **Configurable sans recompilation** : ajouter `Japan` à `countries` ne demande de modifier ni le HTML ni le code Java.
- **Séparation des responsabilités** : le template se contente d'afficher, les données sont dans la configuration.
- **Une seule source de vérité** : la liste est définie à un endroit, pas dupliquée dans plusieurs templates.
- **Adaptation par environnement** (profils Spring, variables d'environnement) sans toucher au code.
- **Moins d'erreurs** et code plus propre qu'avec des dizaines de `<option>` écrites en dur.

## 8. `favoriteSystems` en `List<String>` et `favoriteLanguage` en `String`

Les **boutons radio** partageant le même `name` sont mutuellement exclusifs : au plus **une seule valeur** est envoyée, donc un `String` suffit. Les **cases à cocher** permettent d'en cocher **plusieurs** : le navigateur envoie alors le même paramètre plusieurs fois (`favoriteSystems=Linux&favoriteSystems=macOS`). Spring regroupe ces valeurs dans une collection, d'où le type `List<String>`. Un simple `String` ne pourrait conserver qu'une seule valeur (ou une concaténation inexploitable).
