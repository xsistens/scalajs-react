package japgolly.scalajs.react

import japgolly.scalajs.react.ReactComponentC.{ConstProps, DefaultProps, ReqProps}

import scala.scalajs.js

object ElementFactory {
  /**
   * add types to js constructor
   * @param ctor
   * @tparam P
   * @tparam S
   * @return
   */
  private def getComponentConstructor[P, S, N <: TopNode](ctor: js.Dynamic): ReactClass[P, S, Unit, N] = {
    ctor.asInstanceOf[ReactClass[P, S, Unit, N]]
  }

  def noProps[S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[Unit, S, N]]) = {
    implicit val propsConverter: PropsConverter[Unit] = DefaultUnitPropsConverter
    val ctor = getComponentConstructor[Unit, S, N](cls)
    val factory = React.createFactory[Unit, S, Unit, N](ctor)
    new ConstProps[Unit, S, Unit, N](factory, ctor, js.undefined, js.undefined, () => Unit)
  }

  def constantPropsJS[P <: js.Object, S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[P, S, N]])(props: P) = constantProps(cls, c)(props)(new JSPropsConverter[P])

  def constantProps[P, S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[P, S, N]])(props: P)(implicit propsConverter: PropsConverter[P] = new DefaultPropsConverter[P]) = {
    val ctor = getComponentConstructor[P, S, N](cls)
    val factory = React.createFactory[P, S, Unit, N](ctor)
    new ConstProps[P, S, Unit, N](factory, ctor, js.undefined, js.undefined, () => props)
  }

  def defaultPropsJS[P <: js.Object, S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[P, S, N]])(defaultProps: P) = this.defaultProps(cls, c)(defaultProps)(new JSPropsConverter[P])

  def defaultProps[P, S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[P, S, N]])(defaultProps: P)(implicit propsConverter: PropsConverter[P] = new DefaultPropsConverter[P]) = {
    val ctor = getComponentConstructor[P, S, N](cls)
    val factory = React.createFactory[P, S, Unit, N](ctor)
    new DefaultProps[P, S, Unit, N](factory, ctor, js.undefined, js.undefined, () => defaultProps)
  }

  def requiredPropsJS[P <: js.Object, S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[P, S, N]]) = requiredProps(cls, c)(new JSPropsConverter[P])

  def requiredProps[P, S, N <: TopNode](cls: js.Dynamic, c: Class[_ <: BasicReactComponent[P, S, N]])(implicit propsConverter: PropsConverter[P] = new DefaultPropsConverter[P]) = {
    val ctor = getComponentConstructor[P, S, N](cls)
    val factory = React.createFactory[P, S, Unit, N](ctor)
    new ReqProps[P, S, Unit, N](factory, ctor, js.undefined, js.undefined)
  }
}
