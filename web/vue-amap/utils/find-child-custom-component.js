// weex defines div as custome component so we don't need to loop the div
const findChildCustomComponent = function (component, childComponents = []) {
  if (component['_name'] !== '<Div>' && component['_name'] !== '<Amap>') {
    childComponents = childComponents.concat([component]);
  }
  if (component.$children.length > 0) {
    component.$children.forEach((com) => {
      childComponents = childComponents.concat(findChildCustomComponent(com));
    });
  }
  return childComponents;
};

export default findChildCustomComponent;
