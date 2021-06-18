//
//  CRBaseViewController.h
//  supercam
//
//  Created by UI design on 2016/11/19.
//  Copyright © 2016年 Mouse. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface CRBaseViewController : UIViewController

@property(nonatomic, strong) UITableView *baseTableView; ///<根式图TableView
@property(nonatomic, assign) int pageIndex; ///<分页索引
@property(nonatomic, strong) UIImage *defaultNullDataImage; ///无数据的占位图
@property(nonatomic, strong) NSString *defaultNullTitle; ///无数据的默认标题

//@property (nonatomic, strong) CameraModel *currentDeviceModel; // 当前选择的设备

/**
 根部初始化UITableView

 @param style 类型
 */
- (void)initWithTableViewStyle:(UITableViewStyle)style;
- (void)settingNavcon;///<设置导航条
- (void)setTitleWithString:(NSString *)title color:(UIColor *)color;///<设置标题
- (void)addLeftBtnWithImage:(NSString *)imgName_n h_Image:(NSString *)imgName_h;///<自定义左边图片按钮
- (void)addLeftBtnWithNSString:(NSString *)title;///<自定义左边文字按钮
- (void)addBlackBackBtn;///<添加黑色的返回按钮，默认是白色的
- (void)click_leftBtn;///<左边按钮点击事件
- (void)addRightBtnWithImage:(NSString *)imgName_n h_Image:(NSString *)imgName_h;///<自定义右边边图片按钮
- (void)addRightBtnWithString:(NSString *)title color:(UIColor *)color;///<自定义右边文字按钮
- (UIButton *)addRightBtnWithString:(NSString *)title colorKey:(NSString *)colorKey;///<自定义右边文字按钮
- (void)click_rightBtn;///<导航栏右侧按钮点击事件


/// 添加下拉刷新功能
/// @param headerViewRefresh 回调函数
- (void)addHeaderViewRefresh:(void(^)(void))headerViewRefresh;


/// 添加上提加载下一页功能
/// @param footerViewRefresh 回调函数
- (void)addFooterViewRefresh:(void(^)(void))footerViewRefresh;

#pragma mark ------ 显示空白页，根据数组长度自动显示空白页
- (void)listVisibleShowEmptyViewFromAry:(NSMutableArray*)dataAry;

/**
 导航栏隐藏
 */
- (void)setNavBarHidden;


/// 导航栏背景图片
/// @param image 图片
- (void)setNavBarImage:(UIImage *)image;

/**
 不能侧滑返回,默认是可以返回的，特殊情况不需要再设置
 */
- (void)unScrollPopViewController;


/// 颜色转图片
/// @param color 颜色
- (UIImage *)imageWithColor:(UIColor *)color;

- (void)setStatusStyle:(UIStatusBarStyle)style;

@end
