package japgolly.scalajs.react

import japgolly.scalajs.react

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.{JSName, ScalaJSDefined}

@js.native
@JSName("React.Component")
private[react] abstract class ReactJSComponent[P, S](initialProps: ReactProps) extends js.Object {
  val displayName: String = js.native

  var refs: RefsObject = js.native

  @JSName("state")
  private[react] var _state: WrapObj[S] = js.native

  @JSName("props")
  private[react] def _props: ReactProps with PropsMixedIn = js.native

  def componentWillMount(): Unit = js.native

  def componentDidMount(): Unit = js.native

  def componentWillUnmount(): Unit = js.native

  @JSName("forceUpdate")
  private[react] def _forceUpdate(callback: js.UndefOr[js.Function]): Unit = js.native

  @JSName("componentWillReceiveProps")
  private[react] def _componentWillReceiveProps(nextProps: ReactProps): Unit = js.native

  @JSName("shouldComponentUpdate")
  private[react] def _shouldComponentUpdate(nextProps: ReactProps, nextState: WrapObj[S]): Boolean = js.native

  @JSName("componentWillUpdate")
  private[react] def _componentWillUpdate(nextProps: ReactProps, nextState: WrapObj[S]): Unit = js.native

  @JSName("componentDidUpdate")
  private[react] def _componentDidUpdate(prevProps: ReactProps, prevState: WrapObj[S]): Unit = js.native

  @JSName("setState")
  private[react] def _setState(s: WrapObj[S], callback: UndefOr[js.Function]): Unit = js.native

  @JSName("setState")
  private[react] def _modState(s: js.Function1[WrapObj[S], WrapObj[S]], callback: UndefOr[js.Function]): Unit = js.native
}

@ScalaJSDefined
private[react] abstract class BasicReactComponent[P, S, N <: TopNode](initialProps: ReactProps) extends ReactJSComponent[P, S](initialProps) {
  def getRef[R <: Ref](ref: R) = ref(refs)

  def render(): ReactElement

  @JSName("_forceUpdate")
  def forceUpdate(callback: Callback = Callback.empty): Unit =
    _forceUpdate(callback.toJsCallback)

  def children: PropsChildren = _props.children

  def propsDynamic = _props.asInstanceOf[js.Dynamic]
}

@ScalaJSDefined
abstract class ReactComponentNoProps[S, N <: TopNode] extends BasicReactComponent[Unit, S, N](WrapObj(Unit).asInstanceOf[ReactProps]) {
  def initialState(): S

  @JSName("_state")
  def state: S = _state.v

  @JSName("_modState")
  def modState(func: (S) => S, callback: Callback = Callback.empty): Callback =
    CallbackTo(_modState((s: WrapObj[S]) => WrapObj(func(s.v)), callback.toJsCallback))

  _state = WrapObj(initialState())

  @JSName("_setState")
  def setState(newState: S, callback: Callback = Callback.empty): Callback =
    CallbackTo(_setState(WrapObj(newState), callback.toJsCallback))

  @JSName("componentWillUpdate")
  override def _componentWillUpdate(nextProps: ReactProps, nextState: WrapObj[S]): Unit =
    componentWillUpdate(nextState.v)

  @JSName("_componentWillUpdate")
  def componentWillUpdate(nextState: => S): Unit = ()
}

@ScalaJSDefined
abstract class ReactComponentNoState[P, N <: TopNode](initialProps: ReactProps) extends BasicReactComponent[P, Unit, N](initialProps) {

  def propsConverter: PropsConverter[P] = new DefaultPropsConverter[P]

  private var p: P = propsConverter.fromProps(initialProps)

  @JSName("_props")
  def props: P = p

  @JSName("componentWillReceiveProps")
  override def _componentWillReceiveProps(nextProps: ReactProps): Unit = {
    val newProps = propsConverter.fromProps(nextProps)
    componentWillReceiveProps(newProps)
    p = newProps
  }

  @JSName("_componentWillReceiveProps")
  def componentWillReceiveProps(nextProps: P): Unit = ()

  @JSName("componentWillUpdate")
  override def _componentWillUpdate(nextProps: ReactProps, nextState: WrapObj[Unit]): Unit = {
    componentWillUpdate(propsConverter.fromProps(nextProps))
  }

  @JSName("_componentWillUpdate")
  def componentWillUpdate(nextProps: P): Unit = ()
}

@ScalaJSDefined
abstract class ReactComponentNoPropsAndState[N <: react.TopNode] extends BasicReactComponent[Unit, Unit, N](WrapObj(Unit).asInstanceOf[ReactProps])

@ScalaJSDefined
abstract class ReactComponent[P, S, N <: react.TopNode](initialProps: ReactProps) extends BasicReactComponent[P, S, N](initialProps) {
  def propsConverter: PropsConverter[P] = new DefaultPropsConverter[P]

  private var p: P = propsConverter.fromProps(initialProps)

  _state = {
    WrapObj(initialState(if (js.isUndefined(initialProps) || initialProps == null) throw new Exception("props are not set correctly") else props))
  }

  def initialState(props: P): S

  @JSName("_state")
  def state: S = _state.v

  @JSName("_modState")
  def modState(func: (S) => S, callback: Callback = Callback.empty): Callback =
    CallbackTo(_modState((s: WrapObj[S]) => WrapObj(func(s.v)), callback.toJsCallback))

  @JSName("_props")
  def props: P = p

  @JSName("_setState")
  def setState(newState: S, callback: Callback = Callback.empty): Callback =
    CallbackTo(_setState(WrapObj(newState), callback.toJsCallback))

  @JSName("componentWillReceiveProps")
  override def _componentWillReceiveProps(nextProps: ReactProps): Unit = {
    val newProps = propsConverter.fromProps(nextProps)
    componentWillReceiveProps(newProps)
    p = newProps
  }

  @JSName("_componentWillReceiveProps")
  def componentWillReceiveProps(nextProps: P): Unit = ()

  @JSName("componentWillUpdate")
  override def _componentWillUpdate(nextProps: ReactProps, nextState: WrapObj[S]): Unit = {
    componentWillUpdate(propsConverter.fromProps(nextProps), nextState.v)
  }

  @JSName("_componentWillUpdate")
  def componentWillUpdate(nextProps: P, nextState: S): Unit = ()
}

@ScalaJSDefined
abstract class ReactComponentNoStateJS[P <: js.Object, N <: react.TopNode](initialProps: ReactProps) extends ReactComponentNoState[P, N](initialProps) {
  override def propsConverter: PropsConverter[P] = new JSPropsConverter[P]
}

@ScalaJSDefined
abstract class ReactComponentJS[P <: js.Object, S, N <: react.TopNode](initialProps: ReactProps) extends ReactComponent[P, S, N](initialProps) {
  override def propsConverter: PropsConverter[P] = new JSPropsConverter[P]
}

@js.native
trait WithContext[C] extends js.Any {
  var context: C = js.native
}

@js.native
trait WithChildContext[C] extends js.Any {
  def getChildContext(): C = js.native
}