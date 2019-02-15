exports.defineAutoTests = function () {
  describe('RevenueCatShim', () => {
    it('exists', function () {
      expect(window.RevenueCat).toBeDefined();
    });

    it('has own methods', () => {
      expect(window.RevenueCat.configure).toBeDefined();
      expect(window.RevenueCat.getEntitlements).toBeDefined();
      expect(window.RevenueCat.makePurchase).toBeDefined();
      expect(window.RevenueCat.getPurchaserInfo).toBeDefined();
      expect(window.RevenueCat.identify).toBeDefined();
      expect(window.RevenueCat.reset).toBeDefined();
    })
  });
};
